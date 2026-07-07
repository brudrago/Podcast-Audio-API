(ns audio-api.middleware.error
  (:require [cheshire.core :as json])
  (:import [clojure.lang ExceptionInfo]
           [java.lang IllegalArgumentException]
           [java.time.format DateTimeParseException]
           [org.postgresql.util PSQLException]))

(defn- json-response [status body]
  {:status status
   :headers {"Content-Type" "application/json"}
   :body (json/generate-string body)})

(defn wrap-error-handling [handler]
  (fn [request]
    (try
      (handler request)

      (catch ExceptionInfo exception
        (let [{:keys [type field]} (ex-data exception)]
          (if (= type :validation-error)
            (json-response
              400
              {:error (.getMessage exception)
               :field field})
            (json-response 500 {:error "Internal server error"}))))

      (catch IllegalArgumentException exception
        (json-response
          400
          {:error "Invalid UUID"}))

      (catch DateTimeParseException exception
        (json-response
          400
          {:error "Invalid published date"}))

      (catch PSQLException exception
        (if (= "23505" (.getSQLState exception))
          (json-response
            409
            {:error "Episode already exists"})
          (json-response
            500
            {:error "Database error"})))

      (catch Exception exception
        (json-response
          500
          {:error "Internal server error"})))))
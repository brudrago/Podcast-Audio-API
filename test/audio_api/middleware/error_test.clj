(ns audio-api.middleware.error-test
  (:require
    [clojure.test :refer :all]
    [cheshire.core :as json]
    [audio-api.middleware.error :as error])
  (:import
    [org.postgresql.util PSQLException]))

(defn- parse-body [response]
  (json/parse-string (:body response) true))


(deftest validation-error-test
  (testing "returns 400 for validation errors"
    (let [handler (fn [_]
                    (throw
                      (ex-info
                        "Title is required"
                        {:type :validation-error
                         :field :title})))

          wrapped-handler (error/wrap-error-handling handler)
          response (wrapped-handler {})
          body (parse-body response)]

      (is (= 400 (:status response)))

      (is (= "application/json"
             (get-in response [:headers "Content-Type"])))

      (is (= "Title is required"
             (:error body)))

      (is (= "title"
             (:field body))))))


(deftest invalid-uuid-test
  (testing "returns 400 for invalid UUID"
    (let [handler (fn [_]
                    (throw
                      (IllegalArgumentException.
                        "Invalid UUID")))

          wrapped-handler (error/wrap-error-handling handler)
          response (wrapped-handler {})
          body (parse-body response)]

      (is (= 400 (:status response)))

      (is (= "Invalid UUID"
             (:error body))))))


(deftest duplicate-episode-test
  (testing "returns 409 for PostgreSQL unique constraint violation"
    (let [handler (fn [_]
                    (throw
                      (PSQLException.
                        "Duplicate key"
                        (org.postgresql.util.PSQLState/UNIQUE_VIOLATION))))

          wrapped-handler (error/wrap-error-handling handler)
          response (wrapped-handler {})
          body (parse-body response)]

      (is (= 409 (:status response)))

      (is (= "Episode already exists"
             (:error body))))))


(deftest database-error-test
  (testing "returns 500 for other PostgreSQL errors"
    (let [handler (fn [_]
                    (throw
                      (PSQLException.
                        "Database connection failed"
                        (org.postgresql.util.PSQLState/CONNECTION_FAILURE))))

          wrapped-handler (error/wrap-error-handling handler)
          response (wrapped-handler {})
          body (parse-body response)]

      (is (= 500 (:status response)))

      (is (= "Database error"
             (:error body))))))


(deftest unexpected-error-test
  (testing "returns 500 for unexpected exceptions"
    (let [handler (fn [_]
                    (throw
                      (RuntimeException.
                        "Unexpected error")))

          wrapped-handler (error/wrap-error-handling handler)
          response (wrapped-handler {})
          body (parse-body response)]

      (is (= 500 (:status response)))

      (is (= "Internal server error"
             (:error body))))))
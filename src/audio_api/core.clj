(ns audio-api.core
  (:require
    [compojure.core :refer [GET defroutes]]
    [compojure.route :as route]
    [ring.adapter.jetty :refer [run-jetty]]
    [cheshire.core :as json]
    [audio-api.db :as db]))

(defroutes app-routes

           (GET "/bd-audio" []
                {:status 200
                 :body   "{\"status\":\"ok\"}"})

           (GET "/bd-audio/episodes" []
             {:status  200
              :headers {"Content-Type" "application/json"}
              :body    (json/generate-string {:episodes (db/get-episodes)})})

           (route/not-found
             {:status 404
              :body   "{\"error\":\"Route not found\"}"}))

(defn -main
  [& _args]
  (println "Server running on port 3000")
  (run-jetty app-routes {:port 3000 :join? false}))
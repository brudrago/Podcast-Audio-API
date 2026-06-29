(ns audio-api.core
  (:require
    [compojure.core :refer [GET defroutes]]
    [compojure.route :as route]
    [ring.adapter.jetty :refer [run-jetty]]))

(defroutes app-routes

           (GET "/health" []
                {:status 200
                 :body   "{\"status\":\"ok\"}"})

           (route/not-found
             {:status 404
              :body   "{\"error\":\"Route not found\"}"}))

(defn -main
  [& _args]
  (println "Server running on port 3000")
  (run-jetty app-routes {:port 3000 :join? false}))
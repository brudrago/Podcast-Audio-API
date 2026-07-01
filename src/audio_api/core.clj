(ns audio-api.core
  (:require
    [compojure.core :refer [GET defroutes]]
    [compojure.route :as route]
    [ring.adapter.jetty :refer [run-jetty]]
    [audio-api.handler :as handler]))

(defroutes app-routes
  (GET "/bd-audio"          request (handler/health request))
  (GET "/bd-audio/episodes" request (handler/get-episodes request))
  (GET "/bd-audio/episodes/:id" [id]    (handler/get-episode-by-id id))
  (route/not-found {:status 404 :body "{\"error\":\"Route not found\"}"}))

(defn -main [& _args]
  (println "Server running on port 3000")
  (run-jetty app-routes {:port 3000 :join? false}))
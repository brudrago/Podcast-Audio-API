(ns audio-api.core
  (:require
    [compojure.core :refer [GET POST defroutes]]
    [compojure.route :as route]
    [ring.adapter.jetty :refer [run-jetty]]
    [ring.middleware.json :refer [wrap-json-body]]
    [audio-api.handler :as handler]))

(defroutes app-routes
           (GET "/bd-audio" request
             (handler/health request))

           (GET "/bd-audio/episodes" request
             (handler/get-episodes request))

           (GET "/bd-audio/episodes/:id" [id]
             (handler/get-episode-by-id id))

           (POST "/bd-audio/episodes" request
                 (handler/create-episode request))

           (route/not-found
             {:status 404
              :headers {"Content-Type" "application/json"}
              :body "{\"error\":\"Route not found\"}"}))

(def app
  (wrap-json-body app-routes {:keywords? true}))

(defn -main [& _args]
  (println "Server running on port 3000")
  (run-jetty app {:port 3000 :join? false}))
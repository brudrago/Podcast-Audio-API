(ns audio-api.routes
  (:require
    [compojure.core :refer [GET POST defroutes]]
    [compojure.route :as route]
    [audio-api.handler :as handler]))

(defroutes app-routes
           (GET "/bd-audio" request
             (handler/health request))

           (GET "/bd-audio/episodes" request
             (handler/get-episodes request))

           (GET "/bd-audio/episode/:id" [id]
             (handler/get-episode-by-id id))

           (POST "/bd-audio/episodes" request
             (handler/create-episode request))

           (route/not-found
             {:status 404
              :headers {"Content-Type" "application/json"}
              :body "{\"error\":\"Route not found\"}"}))
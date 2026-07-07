(ns audio-api.handler
  (:require
    [cheshire.core :as json]
    [audio-api.episodes.service :as service]
    [audio-api.episodes.mapper :as mapper]))

(defn- json-response [status body]
  {:status  status
   :headers {"Content-Type" "application/json"}
   :body    (json/generate-string body)})

(defn health [_]
  (json-response 200 {:status "ok"}))

(defn get-episodes [_]
  (json-response 200 {:episodes (mapper/episodes->response (service/list-episodes))}))

(defn get-episode-by-id [id]
  (let [episode (service/get-episode-by-id id)]
    (if episode
      (json-response 200 (mapper/episode->response episode))
      (json-response 404 {:error "Episode not found"}))))


(defn create-episode [request]
  (let [episode (mapper/request->episode (:body request))
        created (service/create-episode episode)]
    (json-response 201 (mapper/episode->response created))))
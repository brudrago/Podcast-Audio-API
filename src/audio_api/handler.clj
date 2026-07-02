(ns audio-api.handler
  (:require
    [cheshire.core :as json]
    [audio-api.episodes.service :as service]))

(defn- json-response [status body]
  {:status  status
   :headers {"Content-Type" "application/json"}
   :body    (json/generate-string body)})

(defn health [_]
  (json-response 200 {:status "ok"}))

(defn get-episodes [_]
  (json-response 200 {:episodes (service/list-episodes)}))

(defn get-episode-by-id [id]
  (let [episode (service/get-episode-by-id id)]
    (if episode
      (json-response 200 episode)
      (json-response 404 {:error "Episode not found"}))))
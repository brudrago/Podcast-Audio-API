(ns audio-api.episodes.service
  (:require [audio-api.episodes.repository :as repository]))

(defn list-episodes []
  (repository/find-all))

(defn get-episode-by-id [id]
  (repository/find-by-id id))

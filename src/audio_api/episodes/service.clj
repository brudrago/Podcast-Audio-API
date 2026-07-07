(ns audio-api.episodes.service
  (:require [audio-api.episodes.repository :as repository])
  (:import (java.time LocalDateTime))
  (:import (java.util UUID)))

(defn list-episodes []
  (repository/find-all))

(defn get-episode-by-id [id]
  (repository/find-by-id (UUID/fromString id)))

(defn- prepare-episode [episode]
  (-> episode
      (update :id UUID/fromString)
      (update :published-at LocalDateTime/parse)))

;tem o ! porque tem efeito colateral, nao é pura
(defn create-episode [episode]
  (repository/create! (prepare-episode episode)))
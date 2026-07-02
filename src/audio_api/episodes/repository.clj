(ns audio-api.episodes.repository
  (:require
    [audio-api.database.connection :as db]))

(defn find-all []
  (db/query
    ["SELECT * FROM episodes ORDER BY published_at DESC"]))

(defn find-by-id [id]
  (db/query-one
    ["SELECT * FROM episodes WHERE id = ?" id]))

(defn create!
  [{:keys [id title description duration-seconds audio-key published-at]}]
  (db/query-one
    ["INSERT INTO episodes
      (id, title, description, duration_seconds, audio_key, published_at)
      VALUES (?::uuid, ?, ?, ?, ?, ?::timestamp)
      RETURNING *"
     id title description duration-seconds audio-key published-at]))
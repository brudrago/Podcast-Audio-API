(ns audio-api.episodes.repository
  (:require
    [next.jdbc :as jdbc]
    [audio-api.database.connection :as db]))

(defn find-all []
  (db/query
    ["SELECT * FROM episodes ORDER BY published_at DESC"]))

(defn find-by-id [id]
  (db/query-one
    ["SELECT * FROM episodes WHERE id = ?" id]))

(defn create!
  [{:keys [id
           title
           description
           duration-seconds
           audio-key
           published-at]}]

  (db/execute
    ["INSERT INTO episodes
      (id,
       title,
       description,
       duration_seconds,
       audio_key,
       published_at)

      VALUES (?, ?, ?, ?, ?, ?)"

     id
     title
     description
     duration-seconds
     audio-key
     published-at]))
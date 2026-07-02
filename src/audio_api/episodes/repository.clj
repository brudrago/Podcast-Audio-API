(ns audio-api.episodes.repository
  (:require
    [next.jdbc :as jdbc]
    [audio-api.database.connection :as db]))

(defn find-all []
  (jdbc/execute!
    (db/get-datasource)
    ["SELECT * FROM episodes ORDER BY published_at DESC"]))

(defn find-by-id [id]
  (jdbc/execute-one!
    (db/get-datasource)
    ["SELECT * FROM episodes WHERE id = ?" id]))

(defn create!
  [{:keys [id
           title
           description
           duration-seconds
           audio-key
           published-at]}]

  (jdbc/execute-one!
    (db/get-datasource)
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
(ns audio-api.database.connection
  (:require [next.jdbc :as jdbc])
  (:import [com.zaxxer.hikari HikariDataSource]))
(ns audio-api.database.connection
  (:require [next.jdbc :as jdbc])
  (:import [com.zaxxer.hikari HikariDataSource]))

(def db-config
  {:jdbcUrl "jdbc:postgresql://localhost:5432/podcast_db"
   :username "podcast_user"
   :password "podcast_password"})

(defonce datasource
         (delay
           (let [ds (HikariDataSource.)]
             (.setJdbcUrl ds (:jdbcUrl db-config))
             (.setUsername ds (:username db-config))
             (.setPassword ds (:password db-config))
             ds)))

(defn get-datasource []
  @datasource)

(defn execute! [sql-params]
  (jdbc/execute! (get-datasource) sql-params))

(defn execute-one! [sql-params]
  (jdbc/execute-one! (get-datasource) sql-params))
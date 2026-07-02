(ns audio-api.database.migrations
  (:require [migratus.core :as migratus]))

(def config
  {:store :database
   :migration-dir "migrations"
   :db {:dbtype "postgresql"
        :host "localhost"
        :port 5432
        :dbname "podcast_db"
        :user "podcast_user"
        :password "podcast_password"}})

(defn migrate []
  (migratus/migrate config))

(defn rollback []
  (migratus/rollback config))
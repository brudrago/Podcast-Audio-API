(ns audio-api.episodes.service
  (:require
    [audio-api.episodes.repository :as repository]
    [clojure.string :as str])
  (:import
    [java.util UUID]
    [java.time LocalDateTime]))

(defn list-episodes []
  (repository/find-all))

(defn get-episode-by-id [id]
  (repository/find-by-id (UUID/fromString id)))

(defn- validate-episode! [{:keys [id title duration-seconds published-at]}]
  (when (str/blank? title)
    (throw (ex-info "Title is required"
                    {:type :validation-error
                     :field :title})))

  (when (or (nil? duration-seconds)
            (not (number? duration-seconds))
            (not (pos? duration-seconds)))
    (throw (ex-info "Duration must be greater than zero"
                    {:type :validation-error
                     :field :duration-seconds})))

  (when (str/blank? id)
    (throw (ex-info "Id is required"
                    {:type :validation-error
                     :field :id})))

  (when (str/blank? published-at)
    (throw (ex-info "Published date is required"
                    {:type :validation-error
                     :field :published-at}))))

(defn- prepare-episode [episode]
  (-> episode
      (update :id UUID/fromString)
      (update :published-at LocalDateTime/parse)))

;tem o ! porque tem efeito colateral, nao é pura
(defn create-episode [episode]
  (validate-episode! episode)
  (repository/create! (prepare-episode episode)))
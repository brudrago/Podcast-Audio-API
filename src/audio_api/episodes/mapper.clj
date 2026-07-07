(ns audio-api.episodes.mapper)

(defn episode->response [episode]
  {:id              (str (:episodes/id episode))
   :title           (:episodes/title episode)
   :description     (:episodes/description episode)
   :durationSeconds (:episodes/duration_seconds episode)
   :audioKey        (:episodes/audio_key episode)
   :publishedAt     (str (:episodes/published_at episode))
   :status          (:episodes/status episode)
   :createdAt       (str (:episodes/created_at episode))
   :updatedAt       (str (:episodes/updated_at episode))})

(defn episodes->response [episodes]
  (map episode->response episodes))

(defn request->episode [request]
  {:id               (:id request)
   :title            (:title request)
   :description      (:description request)
   :duration-seconds (:durationSeconds request)
   :audio-key        (:audioKey request)
   :published-at     (:publishedAt request)})
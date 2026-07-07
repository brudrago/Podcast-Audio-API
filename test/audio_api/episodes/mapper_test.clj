(ns audio-api.episodes.mapper-test
  (:require
    [clojure.test :refer :all]
    [audio-api.episodes.mapper :as mapper])
  (:import
    [java.util UUID]
    [java.time LocalDateTime]))

(deftest request->episode-test
  (testing "converts API camelCase request to internal kebab-case representation"
    (let [request {:id "44444444-4444-4444-4444-444444444454"
                   :title "Mapper Test"
                   :description "Testing request mapper"
                   :durationSeconds 300
                   :audioKey "episodes/mapper/audio.mp3"
                   :publishedAt "2026-07-07T20:00:00"}

          result (mapper/request->episode request)]

      (is (= {:id "44444444-4444-4444-4444-444444444454"
              :title "Mapper Test"
              :description "Testing request mapper"
              :duration-seconds 300
              :audio-key "episodes/mapper/audio.mp3"
              :published-at "2026-07-07T20:00:00"}
             result)))))


(deftest episode->response-test
  (testing "converts database episode to API camelCase response"
    (let [episode {:episodes/id
                   (UUID/fromString
                     "44444444-4444-4444-4444-444444444454")

                   :episodes/title
                   "Mapper Test"

                   :episodes/description
                   "Testing response mapper"

                   :episodes/duration_seconds
                   300

                   :episodes/audio_key
                   "episodes/mapper/audio.mp3"

                   :episodes/published_at
                   (LocalDateTime/parse "2026-07-07T20:00:00")

                   :episodes/status
                   "draft"

                   :episodes/created_at
                   (LocalDateTime/parse "2026-07-07T20:10:00")

                   :episodes/updated_at
                   (LocalDateTime/parse "2026-07-07T20:10:00")}

          result (mapper/episode->response episode)]

      (is (= "44444444-4444-4444-4444-444444444454"
             (:id result)))

      (is (= "Mapper Test"
             (:title result)))

      (is (= 300
             (:durationSeconds result)))

      (is (= "episodes/mapper/audio.mp3"
             (:audioKey result)))

      (is (= "2026-07-07T20:00"
             (:publishedAt result)))

      (is (= "draft"
             (:status result))))))


(deftest episodes->response-test
  (testing "converts a collection of database episodes"
    (let [episode {:episodes/id
                   (UUID/fromString
                     "44444444-4444-4444-4444-444444444454")

                   :episodes/title "Episode"

                   :episodes/description "Description"

                   :episodes/duration_seconds 300

                   :episodes/audio_key "episodes/audio.mp3"

                   :episodes/published_at
                   (LocalDateTime/parse "2026-07-07T20:00:00")

                   :episodes/status "draft"

                   :episodes/created_at
                   (LocalDateTime/parse "2026-07-07T20:10:00")

                   :episodes/updated_at
                   (LocalDateTime/parse "2026-07-07T20:10:00")}]

      (let [result (mapper/episodes->response [episode])]

        (is (= 1 (count result)))

        (is (= "Episode"
               (:title (first result))))

        (is (= 300
               (:durationSeconds (first result))))))))


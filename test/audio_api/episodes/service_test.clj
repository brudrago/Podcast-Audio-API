(ns audio-api.episodes.service-test
  (:require
    [clojure.test :refer :all]
    [audio-api.episodes.service :as service]
    [audio-api.episodes.repository :as repository]))

(deftest create-episode-with-empty-title-test
  (let [episode {:id "44444444-4444-4444-4444-444444444450"
                 :title ""
                 :description "Episode description"
                 :duration-seconds 300
                 :audio-key "episodes/5/audio.mp3"
                 :published-at "2026-07-07T20:00:00"}]

    (testing "throws validation error when title is empty"
      (try
        (service/create-episode episode)
        (is false "Expected validation exception")

        (catch clojure.lang.ExceptionInfo exception
          (is (= "Title is required"
                 (.getMessage exception)))

          (is (= {:type :validation-error
                  :field :title}
                 (ex-data exception))))))))

(deftest create-episode-with-invalid-duration-test
  (let [episode {:id "44444444-4444-4444-4444-444444444451"
                 :title "Episode"
                 :description "Episode description"
                 :duration-seconds 0
                 :audio-key "episodes/5/audio.mp3"
                 :published-at "2026-07-07T20:00:00"}]

    (testing "throws validation error when duration is not positive"
      (try
        (service/create-episode episode)
        (is false "Expected validation exception")

        (catch clojure.lang.ExceptionInfo exception
          (is (= "Duration must be greater than zero"
                 (.getMessage exception)))

          (is (= {:type :validation-error
                  :field :duration-seconds}
                 (ex-data exception))))))))


(deftest create-episode-with-missing-id-test
  (let [episode {:title "Episode"
                 :description "Episode description"
                 :duration-seconds 300
                 :audio-key "episodes/5/audio.mp3"
                 :published-at "2026-07-07T20:00:00"}]

    (testing "throws validation error when id is missing"
      (try
        (service/create-episode episode)
        (is false "Expected validation exception")

        (catch clojure.lang.ExceptionInfo exception
          (is (= "Id is required"
                 (.getMessage exception)))

          (is (= :id
                 (:field (ex-data exception)))))))))


(deftest create-episode-with-missing-published-at-test
  (let [episode {:id "44444444-4444-4444-4444-444444444452"
                 :title "Episode"
                 :description "Episode description"
                 :duration-seconds 300
                 :audio-key "episodes/5/audio.mp3"}]

    (testing "throws validation error when published date is missing"
      (try
        (service/create-episode episode)
        (is false "Expected validation exception")

        (catch clojure.lang.ExceptionInfo exception
          (is (= "Published date is required"
                 (.getMessage exception)))

          (is (= :published-at
                 (:field (ex-data exception)))))))))


(deftest create-valid-episode-test
  (let [episode {:id "44444444-4444-4444-4444-444444444453"
                 :title "Valid Episode"
                 :description "Episode description"
                 :duration-seconds 300
                 :audio-key "episodes/5/audio.mp3"
                 :published-at "2026-07-07T20:00:00"}]

    (testing "converts types and sends episode to repository"
      (with-redefs [repository/create!
                    (fn [prepared-episode]
                      prepared-episode)]

        (let [result (service/create-episode episode)]

          (is (instance? java.util.UUID
                         (:id result)))

          (is (instance? java.time.LocalDateTime
                         (:published-at result)))

          (is (= "Valid Episode"
                 (:title result)))

          (is (= 300
                 (:duration-seconds result))))))))
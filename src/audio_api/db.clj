(ns audio-api.db)

(defn get-episodes []
  [{:id              "6d25c0d2-b42e-4e4b-a9dd-c8e3d42f8b7f"
    :title           "Introdução ao Flutter"
    :description     "Os primeiros passos para construir aplicativos Flutter."
    :durationSeconds 510
    :publishedAt     "2026-06-28T08:00:00Z"
    :coverImageUrl   "https://cdn.meupodcast.com/covers/flutter.png"}
   {:id              "97d38c4d-55c0-44e2-82c2-9fd1d19c8db1"
    :title           "Arquitetura em Clojure"
    :description     "Como organizar projetos backend em Clojure."
    :durationSeconds 1260
    :publishedAt     "2026-06-20T09:30:00Z"
    :coverImageUrl   "https://cdn.meupodcast.com/covers/clojure.png"}])

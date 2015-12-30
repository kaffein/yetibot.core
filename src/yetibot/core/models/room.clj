(ns yetibot.core.models.room
  (:require
    [yetibot.core.adapters.adapter :refer [active-adapters uuid]]
    [taoensso.timbre :refer [debug info warn error]]
    [yetibot.core.config :as config]
    [clojure.string :as s]))

(def config-path [:yetibot :room])

(def room-config-defaults
  "Provides both a list of all available settings as well as their defaults"
  {;; whether to send things like global messages and Tweets to a room
   "broadcast" "false"
   ;; whether to enable "fun" features like image, giphy and meme
   "fun" "true"
   ;; JIRA project
   "jira-project" ""
   ;; default Jenkins project
   "jenkins-default" ""})

(defn merge-on-defaults [room-config]
  (merge room-config-defaults room-config))

(defn settings-by-uuid
  "Returns the full settings map for an adapter given the adapter's uuid."
  [uuid]
  (apply config/get-config (conj config-path uuid)))

(defn settings-for-room [uuid room]
  (merge-on-defaults (get (settings-by-uuid uuid) room {})))

(defn update-settings [uuid room k v]
  (config/apply-config
    (conj config-path uuid room)
    (fn [current-val-if-exists]
      (assoc current-val-if-exists k v)))
  (config/reload-config))
(ns backend.main
  (:gen-class)
  (:require [mount.core :as mount]
            [backend.mount.immutant]
            [clojure.tools.nrepl.server :as rs]
            ;; Don't forget to require all handler namespaces
            [backend.todo]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (do (rs/start-server :bind "0.0.0.0" :port 7088)
    (mount/start)))

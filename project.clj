(defproject tiko-example "0.1.0-SNAPSHOT"
  :description "Simple full-stack Clojure example project"
  :url "https://github.com/MattiNieminen/tiko-example"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.293"]
                 [org.clojure/tools.namespace "0.3.0-alpha3"]
                 [metosin/lokit "0.1.0"]
                 [mount "0.1.10"]
                 [aleph "0.4.2-alpha8"]
                 [ring/ring-defaults "0.3.0-beta1"]
                 [hiccup "1.0.5"]
                 [re-frame "0.8.0"]
                 [reagent "0.6.0"]]
  :main ^:skip-aot backend.main
  :source-paths ["src/clj" "src/cljs" "src/cljc"]
  :test-paths ["test/clj" "test/cljs" "test/cljc"]
  :target-path "target/%s"
  :repl-options {:init-ns user}
  :profiles {:dev {:resource-paths ["target/dev/resources"]
                   :sass {:target-path "target/dev/resources/css"}}
             :uberjar {:aot :all}}
  :plugins [[lein-pdo "0.1.1"]
            [deraen/lein-sass4clj "0.3.0"]
            [lein-figwheel "0.5.8"]]
  :sass {:source-paths ["src/sass"]
         :source-map true
         :output-style :compressed}
  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljc" "src/cljs"]
                        :figwheel true
                        :compiler
                        {:main "frontend.main"
                         :asset-path "js/out"
                         :output-to "target/dev/resources/js/main.js"
                         :output-dir "target/dev/resources/js/out"}}]}
  :figwheel {:css-dirs ["target/dev/resources/css"]}
  :aliases {"develop" ["do" "clean"
                       ["pdo" ["sass4clj" "auto"] ["figwheel"]]]})

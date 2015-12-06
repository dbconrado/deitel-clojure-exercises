(defproject my-stuff "0.1.0"
  :description "(Alguns) Exercicios do Capítulo 11 do Livro do Deitel, 6ª Edição"
  :url "https://github.com/dbconrado/deitel-clojure-exercises"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
  				[seesaw "1.4.5"]]
  :main ^:skip-aot my-stuff.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

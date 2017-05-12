(def jruby-version "1.7.26")
(def jffi-version "1.2.12")
(def jnr-x86asm-version "1.0.2")

(def jruby-core-exclusions
  [com.github.jnr/jffi
   com.github.jnr/jnr-x86asm
   org.ow2.asm/asm
   org.ow2.asm/asm-analysis
   org.ow2.asm/asm-commons
   org.ow2.asm/asm-tree
   org.ow2.asm/asm-util])

(def jffi-deps
  ;; jffi and jnr-x86asm are explicit dependencies because,
  ;; in JRuby's poms, they are defined using version ranges,
  ;; and :pedantic? :abort won't tolerate this.
  [[com.github.jnr/jffi ~jffi-version]
  [com.github.jnr/jffi ~jffi-version :classifier "native"]
  [com.github.jnr/jnr-x86asm ~jnr-x86asm-version]
  [org.jruby/jruby-stdlib ~jruby-version]])

(defproject puppetlabs/jruby-deps "1.7.26-2-SNAPSHOT"
  :description "JRuby dependencies"
  :url "https://github.com/puppetlabs/jruby-deps"
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}

  :min-lein-version "2.7.1"

  :pedantic? :abort

  ;; Note that there is a fair amount of duplication between the
  ;; dependencies listed in this section and the ones in the corresponding
  ;; section under the uberjar profile.  If you make any changes here,
  ;; check the uberjar dependencies to see if the change should be repeated
  ;; there.
  :dependencies (concat [[org.jruby/jruby-core ~jruby-version
                          :exclusions ~jruby-core-exclusions]

                         ;; jruby-core has dependencies on discrete org.ow2.asm
                         ;; artifacts whereas other common Clojure projects like
                         ;; core.async declare a dependency on org.ow2.asm/asm-all,
                         ;; which provides a superset of the content of the
                         ;; discrete org.ow2.asm dependencies.  Defining asm-all
                         ;; here to allows for conflict resolution to be possible
                         ;; in consuming projects.
                         [org.ow2.asm/asm-all "5.0.3"]]
                        ~jffi-deps)

  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :username :env/clojars_jenkins_username
                                     :password :env/clojars_jenkins_password
                                     :sign-releases false}]]

  :profiles {:uberjar
             ;; Dependencies from the project are largely repeated in the
             ;; uberjar dependencies, with the exception that dependencies
             ;; which are commonly used in other jar files which would be used
             ;; with jruby-deps are omitted.  This avoids conflicts which might
             ;; otherwise arise when multiple classpath entries on the Java
             ;; command line provide different versions of the same dependencies.
             ;;
             ;; Jars which are used in conjunction with jruby-deps should
             ;; provide their own versions of the following:
             ;;
             ;; [joda-time]
             ;; [org.ow2.asm/asm-all]
             ;; [org.yaml/snakeyaml]
             {:dependencies ^:replace
              (concat [[org.jruby/jruby-core ~jruby-version
                        :exclusions (concat ~jruby-core-exclusions
                                            [joda-time
                                             org.yaml/snakeyaml])]]
                      ~jffi-deps)}}

  :uberjar-name "jruby-1_7.jar"

  ;; NOTE: jruby-stdlib packages some unexpected things inside
  ;; of its jar.  e.g., it puts a pre-built copy of the bouncycastle
  ;; jar into its META-INF directory.  This is highly undesirable
  ;; for projects that already have a dependency on a different
  ;; version of bouncycastle.  Items below are excluded from the uberjar.
  :uberjar-exclusions  [#"META-INF/jruby.home/lib/ruby/shared/org/bouncycastle"])

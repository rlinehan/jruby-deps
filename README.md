# JRuby dependencies

This project just contains a simple [](project.clj) file for use in packaging
up an uberjar which has just JRuby-specific dependencies that
[Puppet Server](https://github.com/puppetlabs/puppetserver) needs.  The
uberjar specifically excludes some dependencies that Puppet Server already
uses, like `joda-time` and `org.yaml/snakeyaml`, in order to allow Puppet
Server to provide its own preferred versions.  This project allows Puppet
Server's "release" uberjar (which excludes JRuby-specific dependencies) to be
run in combination with the JRuby dependencies uberjar on the same Java
classpath.  This allows the specific version of the JRuby dependencies uberjar
to be swapped out at run-time, e.g., for switching between the use of a
JRuby 1.7 vs. a JRuby 9k dependencies jar.  Dependencies for JRuby 1.7 are
managed in the `1.7.x` branch whereas dependencies for JRuby 9k are managed
in the `9.x` branch.

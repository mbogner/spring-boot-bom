# spring-boot-bom

This is the source of `dev.mbo:spring-boot-bom`. This is meant for typical projects based on spring boot with various
other libraries that don't come with spring boot itself.

## Note for gradle config for publishing:

```shell
cat ~/.gradle/gradle.properties
# signing.keyId=<last8chars>
# signing.password=<key_password>
# signing.secretKeyRingFile=<home>/.gnupg/secring.gpg
# 
# ossrhUsername=<jira-username>
# ossrhPassword=<jira-password>
```

- **keyId** are the last 8 chars of the key to use from gpg -K.
- **password** is the password for that key and
- **secretKeyRingFile** full qualified path to the keyring generated by the command below

list existing keys:

```shell
gpg --list-secret-keys
```

generate keyring file (replace the $KEY_ID with the id of the key to use):

```shell
gpg --keyring secring.gpg --export-secret-keys $KEY_ID > ~/.gnupg/secring.gpg
```

hint: using `~/.gnupg/secring.gpg` in the gradle.properties doesn't work. replace ~ with the full path.

## Steps for publishing

Simply run:

```shell
./gradlew clean signMavenPublication bomZip publishToMavenLocal publish
```

To check the bom before upload simply skip publish.

You can find the files after publish under https://s01.oss.sonatype.org/content/groups/public/dev/mbo/spring-boot-bom/

## Maven Central

This is using OSSRH with login under https://s01.oss.sonatype.org/.
This is legacy and needs migration. The login token for nexus can be downloaded from nexus. Not the same as the
credentials anymore!
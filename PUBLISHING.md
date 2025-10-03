# Guida alla Pubblicazione su Maven Central

## 1. Aggiorna le Informazioni nel build.gradle.kts

Nel file `build.gradle.kts`, sostituisci i seguenti placeholder con i tuoi dati:

```kotlin
url.set("https://github.com/yourusername/java-datocms") // URL del tuo repository
developers {
    developer {
        id.set("yourusername")           // Il tuo username GitHub
        name.set("Your Name")             // Il tuo nome completo
        email.set("your.email@example.com") // La tua email
    }
}
scm {
    connection.set("scm:git:git://github.com/yourusername/java-datocms.git")
    developerConnection.set("scm:git:ssh://github.com/yourusername/java-datocms.git")
    url.set("https://github.com/yourusername/java-datocms")
}
```

## 2. Registra un Account su Sonatype

1. Vai su https://central.sonatype.com/
2. Clicca su "Sign Up" e crea un account
3. Verifica la tua email

## 3. Richiedi il Namespace

### Opzione A: Usare io.github.username (più facile)
Se usi `io.github.tuousername` come groupId, devi solo:
1. Nel `build.gradle.kts` cambia `group = "com.datocms"` in `group = "io.github.tuousername"`
2. Pubblica un repository GitHub pubblico chiamato `java-datocms`
3. Il namespace sarà verificato automaticamente

### Opzione B: Usare com.datocms (richiede verifica dominio)
Per usare `com.datocms`:
1. Devi provare di possedere il dominio datocms.com
2. Aggiungi un record DNS TXT o crea un repository GitHub nell'organizzazione datocms
3. Segui le istruzioni su https://central.sonatype.org/register/namespace/

## 4. Genera una Chiave GPG

```bash
# Genera una nuova chiave
gpg --gen-key

# Segui le istruzioni:
# - Nome: il tuo nome
# - Email: la tua email
# - Password: scegli una password sicura

# Lista le chiavi generate
gpg --list-keys --keyid-format SHORT

# Output simile a:
# pub   rsa3072/ABCD1234 2024-10-03 [SC]
#       1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ
# uid           [ultimate] Your Name <your.email@example.com>

# Prendi nota del KEY_ID (es. ABCD1234)

# Pubblica la chiave su un keyserver
gpg --keyserver keyserver.ubuntu.com --send-keys ABCD1234

# Esporta la chiave privata (necessaria per gradle)
gpg --export-secret-keys -o ~/.gnupg/secring.gpg
```

## 5. Configura le Credenziali

Crea o modifica il file `~/.gradle/gradle.properties`:

```properties
# Credenziali Sonatype
sonatypeUsername=il-tuo-username-sonatype
sonatypePassword=la-tua-password-sonatype

# Configurazione GPG
signing.keyId=ABCD1234
signing.password=password-della-chiave-gpg
signing.secretKeyRingFile=/Users/tuousername/.gnupg/secring.gpg
```

**IMPORTANTE:** NON committare mai questo file nel repository!

## 6. Verifica la Configurazione

Prima di pubblicare, verifica che tutto sia configurato correttamente:

```bash
# Testa la build con signing
./gradlew clean build

# Verifica che i JAR vengano generati correttamente
./gradlew publishToMavenLocal

# Controlla in ~/.m2/repository/com/datocms/java-datocms/
```

## 7. Pubblica su Maven Central

### Per la prima pubblicazione:

```bash
# Assicurati che la versione NON contenga SNAPSHOT
# Nel build.gradle.kts: version = "0.0.1"

# Pubblica
./gradlew publish

# Gli artifact saranno caricati su Sonatype Staging Repository
```

### Login su Sonatype per il Release

1. Vai su https://s01.oss.sonatype.org/
2. Login con le tue credenziali
3. Clicca su "Staging Repositories" nel menu a sinistra
4. Cerca il tuo repository (sarà chiamato qualcosa come `comdatocms-1001`)
5. Selezionalo e clicca "Close"
6. Aspetta qualche minuto per la validazione
7. Se tutto è OK, clicca "Release"
8. Dopo 10-30 minuti sarà disponibile su Maven Central
9. Dopo alcune ore sarà indicizzato su https://search.maven.org

## 8. Pubblicazioni Successive

Per pubblicare nuove versioni:

1. Aggiorna la versione in `build.gradle.kts`:
   ```kotlin
   version = "0.0.2"
   ```

2. Pubblica:
   ```bash
   ./gradlew publish
   ```

3. Ripeti i passi di Close e Release su Sonatype

## Snapshot Versions

Per pubblicare versioni SNAPSHOT (sviluppo):

```kotlin
version = "0.0.2-SNAPSHOT"
```

Le versioni SNAPSHOT vengono pubblicate automaticamente senza bisogno di Close/Release.

## Troubleshooting

### Errore: "401 Unauthorized"
- Verifica username e password in `gradle.properties`
- Assicurati di aver verificato il namespace su Sonatype

### Errore GPG: "secret key not available"
- Verifica che la chiave GPG sia stata esportata correttamente
- Controlla che `signing.keyId` corrisponda alla tua chiave

### Errore: "POM is invalid"
- Assicurati di aver compilato tutti i campi obbligatori nel POM
- Verifica che l'URL del repository sia valido

### Errore: "Checksum validation failed"
- Riprova il comando `./gradlew publish --rerun-tasks`

## Pubblicazione Automatica con GitHub Actions

Il progetto include workflow GitHub Actions per la pubblicazione automatica quando crei un tag.

### Configurazione dei Secrets GitHub

1. Vai sul tuo repository GitHub
2. Clicca su "Settings" → "Secrets and variables" → "Actions"
3. Aggiungi i seguenti secrets:

#### SONATYPE_USERNAME
Il tuo username Sonatype

#### SONATYPE_PASSWORD
La tua password Sonatype

#### GPG_KEY_ID
L'ID della tua chiave GPG (es. ABCD1234)

#### GPG_PASSPHRASE
La password della tua chiave GPG

#### GPG_PRIVATE_KEY
La tua chiave privata GPG in formato base64:

```bash
# Esporta e converti in base64
gpg --armor --export-secret-keys TUO_KEY_ID | base64 | pbcopy

# Su Linux usa:
# gpg --armor --export-secret-keys TUO_KEY_ID | base64 -w 0 | xclip -selection clipboard
```

Incolla il risultato nel secret `GPG_PRIVATE_KEY`

### Come Pubblicare una Nuova Versione

1. Assicurati che tutti i test passino:
   ```bash
   ./gradlew test
   ```

2. Crea e pusha un tag di versione:
   ```bash
   git tag v0.0.1
   git push origin v0.0.1
   ```

3. GitHub Actions automaticamente:
   - Eseguirà i test
   - Builderà il progetto
   - Pubblicherà su Maven Central
   - Creerà una GitHub Release

4. Dopo alcuni minuti, vai su https://s01.oss.sonatype.org/ per completare il release (Close → Release)

### Formato dei Tag

I tag devono seguire il formato `v*.*.*` (es. v0.0.1, v1.0.0, v2.1.3)

### Workflow Disponibili

- **test.yml**: Esegue i test su ogni push e PR al branch main
- **publish.yml**: Pubblica su Maven Central quando viene creato un tag

## Risorse Utili

- [Sonatype Central Portal](https://central.sonatype.com/)
- [Guida Ufficiale](https://central.sonatype.org/publish/publish-guide/)
- [Requisiti POM](https://central.sonatype.org/pages/requirements.html)
- [GPG Signing](https://central.sonatype.org/publish/requirements/gpg/)
- [GitHub Actions](https://docs.github.com/en/actions)

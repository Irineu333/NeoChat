Um chat simples utlizando as tecnologias do Firebase, como Firebase Authentication, Firebase Realtime Database, Firebase Cloud Messaging e Firebase Functions.

### Linguagens

A aplicação é 100% desenvolvida em Kotlin, enquanto o back-end do Functions usa JavaScript, Node.js 12

```kotlin
# login usando FirebaseUI
  override fun requireLogin() {

        inicioView.setVisibilityProgressBar(View.INVISIBLE)

        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build()
        )

        inicioView.requireLogin(providers)
    }
```

Código Fonte em [Projeto NeoChat](https://github.com/Irineu333/NeoChat) e apk em [Releases](https://github.com/Irineu333/NeoChat/releases)

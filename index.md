Um chat simples utlizando as tecnologias do Firebase, como Firebase Authentication, Firebase Realtime Database, Firebase Cloud Messaging e Firebase Functions.

### Arquitetura

## MVP

O projeto utiliza a Arquitetura MVP (Model View Presenter) que visa uma melhor separação de responsabilidades, visualização e regra de negócio, utlizando interfaces.

## Single Activity

Seguindo as recomendações da Google, o projeto é `Single Activity`, possuindo apenas uma Atividade que gerencia as telas através de `Fragmenst`, usando `Navigation Component Architecture`.

### Linguagens

A aplicação é 100% desenvolvida em `Kotlin`, enquanto o back-end do Functions usa `JavaScript` com Node.js 12

#login usando FirebaseUI
```kotlin
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

###Download

Código Fonte em [Projeto NeoChat](https://github.com/Irineu333/NeoChat) e APK em [Releases](https://github.com/Irineu333/NeoChat/releases)

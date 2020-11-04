Um chat simples utlizando as tecnologias do Firebase, como `Firebase Authentication`, `Firebase Realtime Database`, `Firebase Cloud Messaging` e `Firebase Functions`.

# Arquitetura

Um aplicativo sem uma arquitetura é apenas mais um aplicativo. Uma arquitetura não apenas deixa o projeto mais profissional, como também o torna mais legível por outros desenvolvedores e facilita a manutenção. Por isso o NeoChat dois padrões de projeto muito populares, a Arquitetura MVP (Model View Presenter) e Navigation Component Architecture. 

### MVP

O projeto utiliza a Arquitetura MVP (Model View Presenter) que visa uma melhor separação de responsabilidades, visualização e regra de negócio, utlizando interfaces para comunicação entre elas.

### Single Activity

Seguindo as recomendações da Google, o projeto é `Single Activity`, possuindo apenas uma Atividade que gerencia as telas através de `Fragments`, usando `Navigation Component Architecture` e `Safe Args` para navegação entre elas.

## Linguagens

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

# Download

Clone e contribua com o projeto em [Projeto NeoChat](https://github.com/Irineu333/NeoChat)
<br/>Baixe o apk e o projeto em [Releases](https://github.com/Irineu333/NeoChat/releases)

## Download

Um chat simples utlizando as tecnologias do Firebase, como Firebase Authentication, Firebase Realtime Database, Firebase Cloud Messaging e Firebase Functions.

### Linguagens

A aplicação é 100% desenvolvida em Kotlin, enquanto o back-end do Functions usa JavaScript, Node.js 12

```markdown
#kotlin

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

Contribua em [Projeto](https://github.com/Irineu333/NeoChat) e baixe o apk em [Releases](https://github.com/Irineu333/NeoChat/releases)


### Support or Contact

Having trouble with Pages? Check out our [documentation](https://docs.github.com/categories/github-pages-basics/) or [contact support](https://github.com/contact) and we’ll help you sort it out.

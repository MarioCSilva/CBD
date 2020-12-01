# Sistema de mensagens
Foram usadas três estruturas diferentes para o desenvolvimento deste sistema:
-   Uma Hash para guardar a informação do utilizador, como o username e a password.
    Cada utilizador possui uma Hash com a seguinte key `chat:users:<user>`.
    A Hash possui depois os dois campos necessários (username e a password).

-   Um Set para guardar os utilizadores que estão a ser seguidos.
    Visto que não pode seguir o mesmo utilizador várias vezes um Set é mais indicado do que uma Lista.
    Cada utilizador possui um Set com a seguinte key `chat:subs:<user>`

-   Uma Lista para guardar as mensagens enviadas para o sistema.
    Sempre que um utilizador envia uma mensagem, é guardada na lista usando `rpush` para ao ser imprimida na consola ser a última a ser mostrada por ser mais recente.
    Cada utilizador possui uma Lista com a seguinte key `chat:messages:<user>`.
# Backend do Kori

<hr>

## Estrutura dos pacotes na arquitetura MVC

src/
 └── main/
     ├── java/
     │   └── br/com/seuprojeto/
     │       ├── controller/
     │       │   ├── <Classes Servlets>
     │       │   ├── ....
     │       │   └── ....
     │       │
     │       ├── model/
     │       │   ├── entity/
     │       │   │   ├── <Classes da UML da model>
     │       │   │   ├── ....
     │       │   │   └── ....
     │       │   │
     │       │   ├── dao/
     │       │       ├── <Classes de DAO>
     │       │       ├── ....
     │       │       └── ....
     │       │
     │       ├── util/
     │       │   ├── ConnectionFactory.java
     │       │
     │       └── filter/
     │           └── <Classes Filter (se der tempo, podemos fazer)>
     │
     ├── resources/
     │   ├── .env
     │   └── .env.example
     │
     └── webapp/
         ├── WEB-INF/
         │   ├── views/
         │   │   ├── <Páginas JSP>
         │   │   ├── ....
         │   │   └── ....
         │   │
         │   └── web.xml
         │
         ├── css/
         ├── js/
         └── index.jsp

Quando for necessário mexer nos arquivos para modificações ou construção do backend, sigam estas regras da arquitetura.

IMPORTANTE: se o pacote não existir no seu projeto, crie-o de acordo com o exemplo da arquitetura acima.

<hr>

## ConnectionFactory

A ConnectionFactory é uma classe de suporte que serve basicamente como uma fábrica de Connection com o nosso banco de dados, além de deixar o código mais compacto e seguro.  
Ela traz algumas vantagens na hora de criar as classes DAO.

Exemplo:

public class AlunoDAO {
    public void salvar(Aluno aluno) {
        try (
            Connection conn = ConnectionFactory.getConnection(); // Parte importante
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO aluno (nome, nota1, nota2) VALUES (?, ?, ?)"
            )
        ) {
            stmt.setString(1, aluno.getNome());
            stmt.setDouble(2, aluno.getNota1());
            stmt.setDouble(3, aluno.getNota2());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

Quando criamos essa conexão utilizando o ConnectionFactory dentro do try, o mecanismo try-with-resources garante que, ao final da consulta, a conexão seja fechada automaticamente.  
Isso deixa o código mais curto e garante que o banco de dados seja sempre fechado após ser utilizado no método.

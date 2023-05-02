Notas sobre o enunciado
- a alínea 2d) do trabalho, por lapso, não foi dito que também é fornecida a região do jogador. Assim, devem considerar que também é passada a região do jogador.
- Havendo dúvidas se nas questões 2e) e 2f) da 1.ª parte do trabalho se deviam usar ou não a tabela de estatísticas do jogador, os docentes esclarecem que não devem ser usadas tais tabelas. As funções poderão elas próprias ser usadas para povoar aquela tabela.

Alguns de vós detetaram que quando tentavam definir o nível de isolamento dentro de um SP obtinham o erro 25001 com a mensagem 
  "SET TRANSACTION ISOLATION LEVEL must be called before any query" .

Do que nos foi possível apurar, concluímos o seguinte:
Parece que isso se deve a uma limitação do postgresql com os procedimentos armazenados;
O erro ocorre se tentarmos definir um nível de isolamento diferente do "default", o qual pode ser alterado com SET default_transaction_isolation = 'nível de isolamento';
Depois de tudo o que analisámos, a melhor solução que encontrámos, até agora, para os casos em que temos de definir o nível de isolamento, é criar 3 procedimentos armazenados: Um para a lógica, outros para tratamento de erros e controlo transacional (rollback) e outro para definição do nível de isolamento.
Foi colocado um exemplo um exemplo no tópico Recursos Pedagógicos da página principal da UC no moodle. No ponto 1 desse exemplo, reproduz-se a situação de erro e no ponto 2 apresenta-se a solução proposta.

## About error codes
- https://www.postgresql.org/docs/current/errcodes-appendix.html
- https://www.postgresqltutorial.com/postgresql-plpgsql/postgresql-exception/

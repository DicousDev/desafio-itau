package support;


import br.com.projeto.template.model.valueobject.Email;

public class EmailProvider {

    public static Email padrao() {
        return Email.of("joao@gmail.com");
    }
}

package br.landucci.admin.catologo.infrastructure.configuration.usecases;

import br.landucci.admin.catologo.application.castmember.create.CreateCastMemberUseCase;
import br.landucci.admin.catologo.application.castmember.create.DefaultCreateCastMemberUseCase;
import br.landucci.admin.catologo.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import br.landucci.admin.catologo.application.castmember.delete.DeleteCastMemberUseCase;
import br.landucci.admin.catologo.application.castmember.find.DefaultFindCastMemberByIDUseCase;
import br.landucci.admin.catologo.application.castmember.find.FindCastMemberByIDUseCase;
import br.landucci.admin.catologo.application.castmember.list.DefaultListCastMemberUseCase;
import br.landucci.admin.catologo.application.castmember.list.ListCastMemberUseCase;
import br.landucci.admin.catologo.application.castmember.update.DefaultUpdateCastMemberUseCase;
import br.landucci.admin.catologo.application.castmember.update.UpdateCastMemberUseCase;
import br.landucci.admin.catologo.domain.castmember.CastMemberGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CastMemberUseCaseConfig {

    private CastMemberGateway gateway;

    public CastMemberUseCaseConfig(CastMemberGateway gateway) {
        this.gateway = gateway;
    }

    @Bean
    public ListCastMemberUseCase listCastMemberUseCase() {

        return new DefaultListCastMemberUseCase(this.gateway);
    }
    @Bean
    public FindCastMemberByIDUseCase findCastMemberByIDUseCase() {
        return new DefaultFindCastMemberByIDUseCase(this.gateway);
    }
    @Bean
    public CreateCastMemberUseCase createCastMemberUseCase() {
        return new DefaultCreateCastMemberUseCase(this.gateway);
    }
    @Bean
    public UpdateCastMemberUseCase updateCastMemberUseCase() {
        return new DefaultUpdateCastMemberUseCase(this.gateway);
    }
    @Bean
    public DeleteCastMemberUseCase deleteCastMemberUseCase() {
        return new DefaultDeleteCastMemberUseCase(this.gateway);
    }

}
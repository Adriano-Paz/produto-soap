package br.gov.sp.cps.produto_soap.endpoint;

//import br.gov.sp.cps.produto_soap.model.ConsultarProdutoRequest;
//import br.gov.sp.cps.produto_soap.model.ConsultarProdutoResponse;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import br.gov.sp.cps.produto_soap.exception.ProdutoNaoEncontradoException;
import br.gov.sp.cps.produto_soap.schema.ConsultarProdutoRequest;
import br.gov.sp.cps.produto_soap.schema.ConsultarProdutoResponse;




@Endpoint
public class ProdutoEndpoint {

    private static final String NAMESPACE = "http://cps.sp.gov.br/produto";

    @PayloadRoot( namespace = NAMESPACE, localPart = "consultarProdutoRequest"  )
    @ResponsePayload
    public ConsultarProdutoResponse consultarProduto( @RequestPayload ConsultarProdutoRequest request) {

        ConsultarProdutoResponse response =  new ConsultarProdutoResponse();

        if (request.getCodigo() == 1) {

            response.setNome("Arroz");
            response.setDescricao("Cereal");
            response.setMarca("Camil");
            response.setQuantidadeEstoque(5);
            return response;
        } 
        throw new ProdutoNaoEncontradoException(
        		"Produto não encontrado: " + request.getCodigo());
    }
}
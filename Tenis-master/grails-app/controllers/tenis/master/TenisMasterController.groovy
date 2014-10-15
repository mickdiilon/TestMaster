package tenis.master

import javax.servlet.http.HttpServletResponse
import grails.converters.*
import grails.transaction.*
import static org.springframework.http.HttpStatus.*
import static org.springframework.http.HttpMethod.*
import grails.plugin.gson.converters.GSON


class TenisMasterController {

def tenismasterMagamentService


    def notAllowed() {

        def method = request.method

        response.setStatus( HttpServletResponse.SC_METHOD_NOT_ALLOWED)
        response.setContentType "application/json; charset=utf-8"

        def mapResult = [
                message: "Method $method not allowed",
                status: HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                error:"not_allowed"
        ]
        render mapResult as GSON
		
    }



def getTenisMaster() {

        def idtenis = params.idtenis
        def result

        try{

            if (!idtenis){
                render "No escribiste ningun id de tenis"
            } else {
                result = tenismasterMagamentService.getTenisMaster(params.long('idtenis'))
            

            render result as GSON
           }

        }
        catch(Exception e){

            render "Ocurrio un error inesperado"
        }


    }







    def index() { }







}

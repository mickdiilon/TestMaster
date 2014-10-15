package tenis.master

import grails.transaction.Transactional
import java.text.MessageFormat
import org.apache.ivy.plugins.conflict.ConflictManager
import grails.converters.*
import grails.plugin.gson.converters.GSON
//import tenismaster.exceptions.NotFoundException
//import tenismaster.exceptions.ConflictException
//import tenismaster.exceptions.BadRequestException



@Transactional
class TenisMasterService {

static transactional = true

//    def serviceMethod() {

  //  }
	
	
	 def getTenisMaster(def idtenis){

        Map jsonResult = [:]
       

        def tenismasterResult = TenisMaster.findById(idtenis)


        jsonResult.idtenis              = tenismasterResult.idtenis
        jsonResult.nombre               = tenismasterResult.nombre
        jsonResult.marca                = tenismasterResult.marca
        jsonResult.talla                = tenismasterResult.talla
        jsonResult.importacion          = tenismasterResult.importacion

        jsonResult

    }
	
	
	
	
	
}

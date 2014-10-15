package tenis.master

class BootStrap {

    	def init = { servletContext ->
	
	test{
	
	TenisMaster registrado = new TenisMaster(

 idtenis: "123456",
 nombre:'Nike 01',
 marca:'Nike',
 talla:'15',
 importacion:'Si'
  
 ).save(flush:true)

	
	}
	
	production{
	
	}
	
	development{
	
		if (TenisMaster.count() == 0) {
		
		def tenis01 = new TenisMaster(
	
	 idtenis: "00001",
	 nombre:'Venator SX',
	 marca:'Rebook',
	 talla:'16',
	 importacion:'No'
	
	
		)
		
		tenis01.save()
		
		def tenis02 = new TenisMaster(
		 idtenis: "00002",
		 nombre:'Cars Especial',
		 marca:'Rebook',
		 talla:'16',
		 importacion:'No'
		
		)
		
		tenis02.save()
		
		
		}	
	
	}
	
	def destroy = {
	}

		}
}

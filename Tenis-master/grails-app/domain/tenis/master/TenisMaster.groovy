package tenis.master

class TenisMaster {

    static constraints = {

		idtenis         nullable:true, blank:false
        nombre          nullable:false, blank:false, maxSize:256
        marca           blank:false, inList:['Nike','Rebook', 'Adidas']
        talla        	blank:false, minSize:4, inList:['15','16', '17']
        importacion     blank:false, inList:['Si','No']

    }

	String idtenis
    String nombre
    String marca
    String talla
    String importacion


}

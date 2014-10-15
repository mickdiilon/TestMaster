class UrlMappings {

	static mappings = {
       
	   "/$id?" {
	controller = "TenisMaster"
	action = [GET: 'getTenisMaster', POST: 'addTenisMaster', PUT: 'putTenisMaster', DELETE: 'notAllowed']
	}
	   
	   /* "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }*/
       
	   
	   
	   
	    }

       // "/"(view:"/index")
        //"500"(view:'/error')
	//}
	
	
	
	
	
}

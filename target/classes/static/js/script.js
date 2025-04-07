console.log("This is script file...");

// cross bar 
const toggleSideBar =()=>{ 
    if($(".sidebar").is(":visible")){
        console.log("Sidebar true...");
        
        $(".sidebar").css("display","none");
        $(".content").css("margin-left","0%");
 
    }else{
        console.log("Sidebar false...");
        
        $(".sidebar").css("display","block");
        $(".content").css("margin-left","20%");
 
    }
};


function deleteContact( cid ){
	
	
	swal({
	  title: "Are you sure?",
	  text: "Once deleted, you will not be able to recover the contact!!!",
	  icon: "warning",
	  buttons: true,
	  dangerMode: true,
	})
	.then((willDelete) => {
	  if (willDelete) {
		window.location="/user/"+cid+"/deleteContact";

	  } else {
	    swal("Your contact is safe!");
	  }
	});
	
	
	
};


const search=()=>{ 
	let query=$('#search-input').val()
	console.log(query);
	if(query == ''){

		$(".search-result").hide();
	}else{
		// sending request to server 
		let url  =`http://localhost:8080/search/${query}`;

		fetch(url).then((response)=>{
			return response.json();
		}).then((data)=>{
			console.log(data);
			 let text  = `<div  class='list-group' >`
			data.forEach(element => {
				text+=` <a href="/user/${element.cId}/contactPerson" class="list-group-item list-group-item-action" >${element.name}</a>`
			});



			 text+=`</div>`


			$(".search-result").html(text);
			$(".search-result").show();

		})
	}




}










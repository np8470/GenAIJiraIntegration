const input=document.getElementById("searchInput");

input.addEventListener("keyup",function(){

let value=this.value.toLowerCase();

let rows=document.querySelectorAll("#historyTable tr");

rows.forEach(function(row){

row.style.display=

row.innerText.toLowerCase().includes(value)

?

""

:

"none";

});

});

function deleteHistory(id){

if(!confirm("Delete this history?"))

return;

fetch("/api/history/"+id,{

method:"DELETE"

})

.then(()=>location.reload());

}
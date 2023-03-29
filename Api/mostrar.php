<?php
require_once 'conexion.php';

$json = array();

$q = 'SELECT * FROM coordinador';
$query = mysqli_query($con, $q);

while($row = $query->fetch_array()){
    $json[] = array('idC'=>$row['idC'], 'nombres'=>$row['nombres'], 'apellidos'=> $row['apellidos'], 'fechaNac'=>$row['fechaNac'], 'titulo'=>$row['titulo'], 'email'=>$row['email'], 'facultad'=>$row['facultad']);
}

echo json_encode($json);

?>
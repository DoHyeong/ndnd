<?php

	/**
	 *	NaeDonNaeDo prject
	 *	functions.php
	 *
	 *	@author Prev
	 */

	function connect_db() {
		if ($GLOBALS['db_connection']) return $GLOBALS['db_connection'];

		$connection = new mysqli('localhost', DB_USER, DB_PASSWORD, DB_DATABASE);
		$connection->query('SET NAMES utf8');

		$GLOBALS['db_connection'] =  $connection;

		return $connection;
	}

	/*function get_params() {
		$params = \Slim\Slim::getInstance()->request()->getBody();
		$obj = json_decode($params);
		var_dump($params);
		
		return escape_object($obj);
	}*/

	function escape_object($obj) {
		if (gettype($obj) == 'object') {
			foreach ($obj as $key => $val) 
				$obj->{$key} = escape_object($val);
			return $obj;

		}else if (gettype($obj) == 'array') {
			foreach ($obj as $key => $val) 
				$obj[$key] = escape_object($val);
			return $obj;

		}else {
			$db = connect_db();
			return $db->escape_string($obj);
		}
	}

	function manufacture_json($obj) {
		if (gettype($obj) == 'object')
			$new_obj = new StdClass;
		else if (gettype($obj) == 'array')
			$new_obj = array();
		else 
			return $obj;

		foreach ($obj as $key => $val) {
			if ((gettype($val) == 'object' || gettype($val) == 'array') && $val)
				$val = manufacture_json($val);
			else if ($val === 'true' || $val === true)
				$val = true;
			else if ($val === 'false' || $val === false)
				$val = false;
			else if ((string)(int)$val === (string)$val && strlen($val) <= 10)
				$val = intval($val);


			if (gettype($obj) == 'object')
				$new_obj->{ to_cammel_case($key) } = $val;
			else if (gettype($obj) == 'array')
				$new_obj[$key] = $val;
		}

		return $new_obj;
	}


	function throw_object($json_object, $manufacture=TRUE) {
		$response = $GLOBALS['app']->response();
		$response['Content-Type'] = 'application/json';
		$response->status(200);

		if ($manufacture)
			$json_object = manufacture_json($json_object);

		echo json_encode($json_object);
	}


	function get_error_objet($code, $message, $encode_json=false) {
		$obj = new stdClass();
		$obj->result = 'fail';
		$obj->error = new stdClass();
		$obj->error->code = $code;
		$obj->error->message = $message;
		$obj->error->toString = $code . ' ' . $message;
		$obj->data = null;

		if (!$encode_json)
			return $obj;
		else
			return json_encode($obj);
	}

	function throw_error($code, $message=NULL) {
		$response = $GLOBALS['app']->response();
		$response['Content-Type'] = 'application/json';
		$response->status(403);

		if (!$message && $GLOBALS['error_messages'][$code])
			$message = $GLOBALS['error_messages'][$code];

		echo get_error_objet($code, $message, true);
	}

	function to_cammel_case($key) {
		return preg_replace_callback('/[_-]([a-z])/', '_cammel_rep', $key);
	}

	/*function to_cammel_case($obj, $key) {
		$newKey =  preg_replace_callback('/[_-]([a-z])/', '_cammel_rep', $key);

    	$obj->{$newKey} = $obj->{$key};
    	unset( $obj->{$key} );
	}*/

	function _cammel_rep($matches) {
		return strtoupper($matches[1]);
	}



	function log_f($string) {
		if (gettype($string) == 'array' || gettype($string) == 'object')
			$string = json_encode($string);

		$fp = fopen(BASE_DIR . '/test.log', 'a');
		$str = date("Y-m-d H:i:s") . "\n" .
				$string . "\n" . 
				"-----------------------------------------------\n\n";

		fwrite($fp, $str);
		fclose($fp);

	}


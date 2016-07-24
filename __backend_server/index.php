<?php
	
	/**
	 *	NaeDonNaeDo [ndnd]
	 *
	 *	SW Maestro 7th Project
	 *		
	 *	@author 	Prev (prevdev@gmail.com)
	 *	@version 	0.0.1
	 */


	//---------------------------------------------------
	//	PHP preferences
	//--------------------------------------------------
	session_cache_limiter(false);
	session_start();
	date_default_timezone_set('Asia/Seoul');
	error_reporting(E_ALL & ~E_NOTICE);

	define('BASE_DIR', dirname(__FILE__));

	//---------------------------------------------------
	//	Load libs
	//---------------------------------------------------
	require 'Slim/Slim.php';

	require 'db_config.php';
	require 'src/functions.php';
	require 'src/facebook_functions.php';
	require 'src/strings.php';

	require 'models/user.model.php';


	//---------------------------------------------------
	//	Slim Initialize
	//---------------------------------------------------
	\Slim\Slim::registerAutoloader();

	$app = new \Slim\Slim();
	$app->config('debug', true);



	
	//---------------------------------------------------
	// RESTFul API
	//---------------------------------------------------

	$app->get('/load', function() {
		$db = connect_db();
		$access_token = $db->escape_string($_GET['access_token']);

		if (!$access_token) {
			throw_error(ERROR_TOKEN);
			return;
		}

		if ($user = UserModel::auth_user($access_token)) {
			$output = new StdClass;
			$output->user = $user;
			$output->data = array();
			$output->summary = new StdClass;
			$output->summary->lend = 0;
			$output->summary->loan = 0;

			// Get data by user_id
			$cursor = $db->query("SELECT
				`ndnd_data`.*,
				`ndnd_user`.`type` AS target_type,
				`ndnd_user`.`social_uid` AS target_social_uid, 
				`ndnd_user`.`user_name` AS target_user_name2,
				`ndnd_user`.`user_email` AS target_user_email
			FROM `ndnd_data`
			LEFT JOIN ndnd_user
				ON ndnd_data.target_user_id = ndnd_user.id
			WHERE ndnd_data.user_id = '{$user->id}'
			ORDER BY ndnd_data.date DESC");


			while ($row = $cursor->fetch_object()) {
				$obj = new stdClass;
				$obj->target_user = new stdClass;
				
				foreach ($row as $key => $val) {
					if ($key == 'target_user_name2') {
						if ($val != NULL) $key = 'target_user_name';
						else continue;
					}

					if ($key == 'target_user_id')
						$obj->target_user->id = $val;

					else if (strpos($key, 'target_') === 0) {
						$nkey = substr($key, 7);
						$obj->target_user->{$nkey} = $val;
					
					}else
						$obj->{$key} = $val;
				}


				array_push($output->data, $obj);

				if ($row->state == 0) {
					if ($row->type == 0 )
						$output->summary->lend += (int) $row->amount;
					else if ($row->type == 1)
						$output->summary->loan += (int) $row->amount;
				}
			}
			
			throw_object($output);
		}
	});


	$app->post('/data', function () {
		$db = connect_db();
		$access_token = $db->escape_string($_REQUEST['access_token']);

		if (!$access_token) {
			throw_error(ERROR_TOKEN);
			return;
		}

		if (!isset($_POST['type']) || !isset($_POST['amount']) || !isset($_POST['note']) || (!isset($_POST['target_social_uid']) && !isset($_POST['target_user_name']))) {
			throw_error(ERROR_NO_DATA);
			return;
		}

		if ($user = UserModel::auth_user($access_token)) {

			$target_social_uid = $db->escape_string($_POST['target_social_uid']);
			$target_user_name = $db->escape_string($_POST['target_user_name']);

			$type = $db->escape_string($_POST['type']);
			$amount = $db->escape_string($_POST['amount']);
			$note = $db->escape_string($_POST['note']);
			$date = $db->escape_string($_POST['date']);
			$location = $db->escape_string($_POST['location']);


			if ($target_social_uid) {
				$target_user = UserModel::get_user_by_facebook($target_social_uid);
				$target_user_id_v = "'{$target_user->id}'";
				$target_user_name_v = 'NULL';

			}else {
				$target_user_id_v = 'NULL';
				$target_user_name_v = "'{$target_user_name}'";
			}


			$cursor = $db->query("
				INSERT INTO `ndnd_data`
					(`user_id`, `target_user_id`, `target_user_name`, `type`, `state`, `amount`, `note`, `date`, `location`)
				VALUES ('{$user->id}', {$target_user_id_v}, {$target_user_name_v}, '{$type}', '0', '{$amount}', '{$note}', '{$date}', '{$location}')");
			

			if (!$cursor) {
				throw_error(ERROR_QUERY);
				return;
			}

			$output = new StdClass;
			$output->success = true;

			throw_object($output);

		}
	});


	$app->put('/data/:id', function ($dataId) {
		$db = connect_db();
		$access_token = $db->escape_string($_REQUEST['access_token']);

		if (!$access_token) {
			throw_error(ERROR_TOKEN);
			return;
		}

		if ($user = UserModel::auth_user($access_token)) {
			$state = $db->escape_string($_REQUEST['state']);

			$cursor = $db->query("SELECT * FROM `ndnd_data` WHERE `id` = '{$dataId}'");

			if (!$cursor->num_rows) {
				throw_error(ERROR_NO_DATA);
				return;
			}

			$result = $cursor->fetch_object();

			if ($result->user_id != $user->id) {
				throw_error(ERROR_PERMISSION);
				return;
			}

			$cursor2 = $db->query("UPDATE `ndnd_data` SET `state` = '{$state}' WHERE `id` = '{$result->id}'");

			if (!$cursor2) {
				throw_error(ERROR_QUERY);
				return;
			}

			$output = new StdClass;
			$output->success = true;
			throw_object($output);
		}

	});



	$app->run();


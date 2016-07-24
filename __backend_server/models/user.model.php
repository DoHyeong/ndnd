<?php
	
	/**
	 *	NaeDonNaeDo prject
	 *	UserModel class
	 *
	 *	@author Prev
	 */

	class UserModel {

		static public function auth_user($access_token) {
			$db = connect_db();

			$graph_data = fb_api('/me?fields=email,name', $access_token);

			if (!$graph_data) {
				throw_error(ERROR_FACEBOOK);
				return false;
			
			}else {

				$get_user_query = "SELECT * FROM `ndnd_user` WHERE `social_uid` = '{$graph_data->id}'";
				$cursor = $db->query($get_user_query);


				// Insert user if user not exists
				if (!$cursor->num_rows) {
					$cursor2 = $db->query("
						INSERT INTO `ndnd_user` (`type`, `social_uid`, `user_email`, `user_name`, `regdate`)
						VALUES ('facebook', '{$graph_data->id}', '{$graph_data->email}', '{$graph_data->name}', now())
					");

					if (!$cursor2) {
						throw_error(ERROR_QUERY);
						return false;
					}
					$cursor = $db->query($get_user_query);
				
				}
				
				return $cursor->fetch_object();
			}
		}


		static public function get_user_by_facebook($facebook_uid) {
			$db = connect_db();

			$get_user_query = "SELECT * FROM `ndnd_user` WHERE `social_uid` = '{$facebook_uid}'";
			$cursor = $db->query($get_user_query);

			if (!$cursor->num_rows) {
				throw_error(ERROR_NO_DATA);
				return NULL;

			}else
				return $cursor->fetch_object();

		}

	}
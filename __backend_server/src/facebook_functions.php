<?php
	
	/**
	 *	NaeDonNaeDo prject
	 *	facebook_functions.php
	 *
	 *	@author Prev
	 */


	define('FB_GRAPH_API_URL', 'https://graph.facebook.com/v2.7');



	function fb_api($api, $access_token=NULL) {
		$url = FB_GRAPH_API_URL . $api;

		$url .= (strpos($api, '?') === false) ? '?' : '&';
		$url .= 'locale=kr';
		if ($access_token) $url .= '&access_token=' . $access_token;

		$content = @file_get_contents($url);

		if ($content === false) return false;
		else return json_decode($content);
	}
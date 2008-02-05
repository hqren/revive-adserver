<?php

/*
+---------------------------------------------------------------------------+
| Openads v${RELEASE_MAJOR_MINOR}                                                              |
| ======${RELEASE_MAJOR_MINOR_DOUBLE_UNDERLINE}                                                                 |
|                                                                           |
| Copyright (c) 2003-2008 m3 Media Services Ltd                             |
| For contact details, see: http://www.openx.org/                           |
|                                                                           |
| This program is free software; you can redistribute it and/or modify      |
| it under the terms of the GNU General Public License as published by      |
| the Free Software Foundation; either version 2 of the License, or         |
| (at your option) any later version.                                       |
|                                                                           |
| This program is distributed in the hope that it will be useful,           |
| but WITHOUT ANY WARRANTY; without even the implied warranty of            |
| MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the             |
| GNU General Public License for more details.                              |
|                                                                           |
| You should have received a copy of the GNU General Public License         |
| along with this program; if not, write to the Free Software               |
| Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA |
+---------------------------------------------------------------------------+
$Id$
*/

/**
 * @package    OpenadsPlugin
 * @subpackage InvocationTags
 * @author     Radek Maciaszek <radek@m3.net>
 * @author     Andrew Hill <andrew@m3.net>
 *
 */

require_once MAX_PATH . '/plugins/invocationTags/InvocationTags.php';
require_once MAX_PATH . '/lib/max/Plugin/Translation.php';

/**
 *
 * Invocation tag plugin.
 *
 */
class Plugins_InvocationTags_xmlrpc_xmlrpc extends Plugins_InvocationTags
{

    /**
     * Return name of plugin
     *
     * @return string
     */
    function getName()
    {
        return MAX_Plugin_Translation::translate('XML-RPC Tag', $this->module, $this->package);
    }

    /**
     * Return setting configuration file code - required for plugins
     * that store a value in the configuration file.
     *
     * Value returned should be NULL if the plugin does not store
     * a value in the configuration file, otherwise it should be a
     * string in the form "level_key".
     *
     * @return string The setting "code".
     */
    function getSettingCode()
    {
        return 'allowedTags_xmlrpc';
    }

    /**
     * Check if plugin is allowed
     *
     * @return boolean  True - allowed, false - not allowed
     */
    function isAllowed($extra)
    {
        $isAllowed = parent::isAllowed($extra);
        return $isAllowed;
    }

    /**
     * Return list of options
     *
     * @return array    Group of options
     */
    function getOptionsList()
    {
        $options = array (
            'spacer'      => MAX_PLUGINS_INVOCATION_TAGS_STANDARD,
            'what'          => MAX_PLUGINS_INVOCATION_TAGS_STANDARD,
            'campaignid'    => MAX_PLUGINS_INVOCATION_TAGS_STANDARD,
            'target'        => MAX_PLUGINS_INVOCATION_TAGS_STANDARD,
            'source'        => MAX_PLUGINS_INVOCATION_TAGS_STANDARD,
            'withtext'      => MAX_PLUGINS_INVOCATION_TAGS_STANDARD,
            'block'         => MAX_PLUGINS_INVOCATION_TAGS_STANDARD,
            'blockcampaign' => MAX_PLUGINS_INVOCATION_TAGS_STANDARD,
            'hostlanguage'  => MAX_PLUGINS_INVOCATION_TAGS_STANDARD,
            'xmlrpcproto'   => MAX_PLUGINS_INVOCATION_TAGS_STANDARD,
            'xmlrpctimeout' => MAX_PLUGINS_INVOCATION_TAGS_STANDARD,
            'comments'      => MAX_PLUGINS_INVOCATION_TAGS_STANDARD,
        );

        return $options;
    }

    /**
     * Return invocation code for this plugin (codetype)
     *
     * @return string
     */
    function generateInvocationCode()
    {
        parent::prepareCommonInvocationData();

        $conf = $GLOBALS['_MAX']['CONF'];
        $mi = &$this->maxInvocation;

        if (!isset($mi->clientid) || $mi->clientid == '') {
            $mi->clientid = 0;
        }
        if (!isset($mi->campaignid) || $mi->campaignid == '') {
            $mi->campaignid = 0;
        }
        if ($mi->xmlrpcproto) {
            $mi->params = parse_url(MAX_commonConstructSecureDeliveryUrl($conf['file']['xmlrpc']));
        } else {
            $mi->params = parse_url(MAX_commonConstructDeliveryUrl($conf['file']['xmlrpc']));
        }
        if (!$mi->xmlrpctimeout) {
            $mi->timeout = 15;
        } else {
            $mi->timeout = $mi->xmlrpctimeout;
        }
        switch($mi->hostlanguage) {
            case 'php':
            default:
                if (!isset($mi->what) or ($mi->what == "")) {
                    // Need to generate the waht variable here
                    if (isset($mi->zoneid) and ($mi->zoneid != "")) {
                        $mi->what = "zone:" . $mi->zoneid;
                    }elseif (isset($mi->bannerid) and ($mi->bannerid != "")) {
                        $mi->what = "bannerid:" . $mi->bannerid;
                    }
                }

                if (!isset($mi->campaignid)) {
                    $mi->campaignid = 0;
                }

                $buffer .= "<"."?php\n /* " . str_replace(array("\n", '/*', '*/'), array('', '', ''), $mi->buffer) . "\n  *";
                if (!isset($mi->comments) || ($mi->comments == "1")) {
                    $buffer .= MAX_Plugin_Translation::translate('PHP Comment', $this->module, $this->package) . "\n\n";
                }
                $buffer .= '    //ini_set(\'include_path\', \'.:/usr/local/lib\');' . "\n";
                $buffer .= '    require \'openads-xmlrpc.inc.php\';' . "\n\n";
                $buffer .= '    if (!isset($OA_context)) $OA_context = array();' . "\n\n";
                $buffer .= '    $oaXmlRpc = new OA_XmlRpc(\'' . $mi->params['host'] . '\', \'' . $mi->params['path'] . '\'';
                if (isset($mi->params['port'])) {
                    $buffer .= ', ' . $mi->params['port'] . '';
                } else {
                    $buffer .= ($mi->xmlrpcproto) ? ', 443' : ', 80';
                }
                if ($mi->xmlrpcproto) {
                    $buffer .= ', true';
                } else {
                    $buffer .= ', false';
                }
                $buffer .= ', ' . $mi->timeout . ');' . "\n";
                $buffer .= '    $adArray = $oaXmlRpc->view(\'' .
                    $mi->what . '\', ' .
                    $mi->campaignid . ', \'' .
                    $mi->target . '\', \'' .
                    $mi->source . '\', ' .
                    $mi->withtext . ', $OA_context);' . "\n";
                if (isset($mi->block) && $mi->block == '1') {
                    $buffer .= '    $OA_context[] = array(\'!=\' => \'bannerid:\'.$adArray[\'bannerid\']);' . "\n";
                }
                if (isset($mi->blockcampaign) && $mi->blockcampaign == '1') {
                    $buffer .= '    $OA_context[] = array(\'!=\' => \'campaignid:\'.$adArray[\'campaignid\']);' . "\n";
                }
                $buffer .= "\n";
                $buffer .= '    echo $adArray[\'html\'];' . "\n";
                $buffer .= "?".">\n";
                break;
        }

        return $buffer;
    }

}

?>
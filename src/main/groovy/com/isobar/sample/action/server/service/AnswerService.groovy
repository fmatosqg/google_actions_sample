package com.isobar.sample.action.server.service

import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by fabio.goncalves on 6/06/2017.
 */
class AnswerService {

    final Logger LOGGER = LoggerFactory.getLogger(AnswerService)

    protected JSONObject answer(String response) {

        def messages = [[type: 0, speech: "some speech"]]

        JSONObject responseObject = [speech     : response,
                                     displayText: response,
                                     data       : [slack: [
                                             text: response
                                     ]
                                     ]

        ]

        LOGGER.info("Response  " + responseObject)

        return responseObject
    }

    protected JSONObject answer(String response, String slackAttachment) {

        def slackButtons =
                [
                        [text   : "title",
                         actions: [
                                 [name : 'n',
                                  text : 'a',
                                  type : 'button',
                                  value: 'a'],
                                 [name : 'n',
                                  text : 'b',
                                  type : 'button',
                                  value: 'b']
                         ]
                        ]
                ]

        def richResponse = [items:
                                    [[simple_response: [
                                            text_to_speech: response + ' assistant']
                                     ],
                                     [basic_card: [
                                             image: [
                                                     url               : "http://rathankalluri.com/tr-in/agents/tr-1024.jpg",
                                                     accessibility_text: 'sample image'
                                             ]
                                     ]
                                     ]
                                    ]
        ]

        // according to https://developers.google.com/actions/reference/v1/apiai-webhook
        def googleAssistantData = [
                rich_response: richResponse
        ]

        JSONObject responseObject = [speech     : response,
                                     displayText: response,
                                     data       : [
                                             slack : [
                                                     text       : response + ' slack',
                                                     attachments: slackButtons],
                                             google: googleAssistantData],

        ]

        LOGGER.info("Response  " + responseObject)
        return responseObject
    }
}

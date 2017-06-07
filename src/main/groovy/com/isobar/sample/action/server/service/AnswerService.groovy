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

        // basic card defined here: https://developers.google.com/actions/reference/rest/Shared.Types/AppResponse#BasicCard
        def richResponse = [items      :
                                    [[simple_response: [
                                            text_to_speech: response + ' assistant']
                                     ],
                                     [basic_card: [
                                             image  : [
                                                     url               : "http://rathankalluri.com/tr-in/agents/tr-1024.jpg",
                                                     accessibility_text: 'sample image'
                                             ],
                                             buttons: [
                                                     [title          : 'click me',
                                                      open_url_action: [
                                                              url: 'https://www.cnn.com'
                                                      ]]
                                             ]
                                     ]
                                     ]
                                    ],
                            suggestions: [
                                    [title: '1 second'],
                                    [title: '2 seconds']
                            ]
        ]

        def googleAssistantData = [
                rich_response: richResponse
        ]

        JSONObject responseObject = [speech     : response,
                                     displayText: response,
                                     data       : [
                                             slack : [
                                                     text       : response + ' slack',
                                                     attachments: slackButtons],
                                             google: googleAssistantData]

        ]

        LOGGER.info("Response  " + responseObject)
        return responseObject
    }

    protected JSONObject answerWithSuggestions(String text, String userId, List suggestions) {


        def ff = suggestions
                .collect { s -> [title: s] }

        def richResponse = [items      :
                                    [[simple_response: [
                                            text_to_speech: text]
                                     ]
                                    ],
                            suggestions: ff
        ]


        def googleAssistantData = [
                rich_response: richResponse
        ]

        ////

        def slackActionss = [
                [name : 'n',
                 text : 'a',
                 type : 'button',
                 value: 'a'],
                [name : 'n',
                 text : 'b',
                 type : 'button',
                 value: 'b']
        ]

        def slackActions = suggestions
                .collect { s -> [name: 'n', text: s, type: 'button', value: s] }

        def slackButtons =
                [
                        [text       : "Suggestions:",
                         actions    : slackActions,
                         callback_id: userId
                        ]

                ]

        def slackData = [
                text       : text,
                attachments: slackButtons]

        JSONObject responseObject = [speech: text,
                                     data  : [
                                             slack : slackData,
                                             google: googleAssistantData]
        ]
    }
}

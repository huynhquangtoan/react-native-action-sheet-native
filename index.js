/* @flow */

import React from 'react'
import { Platform, ActionSheetIOS, NativeModules } from 'react-native'
const AndroidActionSheet = NativeModules.AndroidActionSheet
const isIOS = Platform.OS === 'ios'
const ActionSheetNaive = isIOS ? ActionSheetIOS : AndroidActionSheet

const optionNames = [
  'title',
  'message',
  'options',
  'tintColor',
  'cancelButtonIndex',
  'destructiveButtonIndex',
  'anchor',
]

export default class ActionSheet extends React.Component {
  componentDidMount() {
    const options = this.props.options
    if (!this.isArray(options) || options.length === 0) {
      throw Error('Prop `options` must be an array and it must not be empty.')
    }
  }
  isArray(obj) {
    return Object.prototype.toString.call(obj) === '[object Array]'
  }
  show() {
    let props = this.props
    const {
      title,
      message,
      options,
      tintColor,
      cancelButtonIndex,
      destructiveButtonIndex,
      anchor,
      onPress,
    } = this.props
    let params = { ...this.props }
    if (isIOS)
      ActionSheetNaive.showActionSheetWithOptions(params, props.onPress)
    else {
      let customTitle = {
        title: title,
        titleColor: '#8f8f8f',
      }
      let cancelBtn = {
        btnTitle: options[cancelButtonIndex],
        btnTitleColor: tintColor ? tintColor : '#157efb',
      }

      let optionBtns = []
      options.map((item, index) => {
        if (index !== cancelButtonIndex) {
          optionBtns.push({
            btnTitle: item,
            btnTitleColor: tintColor
              ? tintColor
              : index === destructiveButtonIndex ? '#fc3d39' : '#157efb',
            btnIndex: index,
          })
        }
      })
      params = {
        title: customTitle,
        optionBtns: optionBtns,
        cancelButtonIndex: cancelButtonIndex,
      }
      if (
        typeof cancelButtonIndex !== 'undefined' &&
        cancelButtonIndex !== null
      ) {
        params.cancelBtn = cancelBtn
      }
      ActionSheetNaive.showActionSheetWithCustomOptions(params, onPress)
    }
  }
  render() {
    return null
  }
}

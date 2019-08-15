/* @flow */

import React from 'react'
import { Platform, ActionSheetIOS, NativeModules } from 'react-native'
const isIOS = Platform.OS === 'ios'
const ActionSheetNative = isIOS ? ActionSheetIOS : NativeModules.ActionSheet

const optionNames = [
  'title',
  'message',
  'options',
  'tintColor',
  'cancelButtonIndex',
  'destructiveButtonIndex',
]

type Props = {
  title?: string,
  message?: string,
  options: Array<string>,
  tintColor?: string,
  cancelButtonIndex?: number,
  destructiveButtonIndex?: number,
  onPress: Function,
};

export default class ActionSheet extends React.Component<Props> {
  componentDidMount() {
    const options = this.props.options
    if (!this.isArray(options) || options.length === 0) {
      throw Error('Prop `options` must be an array and it must not be empty.')
    }
  }
  shouldComponentUpdate() {
    return false // handle on native thread only
  }
  isArray(obj) {
    return Object.prototype.toString.call(obj) === '[object Array]'
  }
  show() {
    const {
      title,
      options,
      tintColor,
      cancelButtonIndex,
      destructiveButtonIndex,
      onPress,
      message
    } = this.props
    if (isIOS) {
      ActionSheetNative.showActionSheetWithOptions({ ...this.props }, onPress)
    }
    else {
      const items = []
      options.forEach((option, index) => {
        if (index !== cancelButtonIndex) {
          items.push({
            title: option
          })
        }
      });
      ActionSheetNative.SheetView(
        {
          title: title,
          items: items,
          titleTextColor: '#000000',
          itemTextColor: '#000000',
          itemTintColor: tintColor || '#000000',
          backgroundColor: '#FFFFFF',
          delayDismissOnItemClick: false,
        },
        selectedIndex => {
          if (typeof cancelButtonIndex !== 'undefined' && cancelButtonIndex !== null) {
            onPress(selectedIndex + 1)
          } else {
            onPress(selectedIndex)
          }
        },
        () => {
          onPress(cancelButtonIndex)
        }
      );
    }
  }
  render() {
    return null
  }
}

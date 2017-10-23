# react-native-action-sheet-native
react-native-action-sheet-native is a JavaScript library for [React Native](https://facebook.github.io/react-native/),
it implements AcionSheet for Android relys on [AndroidActionSheet](https://github.com/zongjingyao/AndroidActionSheet).

### Screenshots
#### IOS
![IOS](https://i.imgur.com/bQhNtKR.png)

#### ANDROID
![ANDROID](https://i.imgur.com/X2Z3tWC.png)

### Installation
```bash
npm install react-native-action-sheet-native --save
react-native link react-native-action-sheet-native
```

### Usage

```js
...
import ActionSheet from 'react-native-action-sheet-native'
...

const IMAGE_PICKER_OPTIONS = ['Quay lại', 'Thư viện ảnh', 'Chụp ảnh mới']

...

  render() {
    return (
      <View style={styles.container}>
        <TouchableOpacity onPress={() => this.actionSheetRef.show()}>
          <Text>Show</Text>
        </TouchableOpacity>
        <Text> Selected index: {`${this.state.index}`}</Text>
        <ActionSheet
          cancelButtonIndex={0}
          destructiveButtonIndex={1}
          options={IMAGE_PICKER_OPTIONS}
          ref={actionSheetRef => (this.actionSheetRef = actionSheetRef)}
          title={'Chọn ảnh từ:'}
          onPress={index => this.setState({ index })}
        />
      </View>
    )
  }
 
...
```
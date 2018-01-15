<!-- $theme: gaia -->
<!-- page_number: true -->
<!-- *page_number: false -->
<!-- footer: リファクタリング入門 -->

# リファクタリング入門
〜OOPらしく書くとコードはどう変わるか〜

---
## 題材
パスワードポリシー

---
## Goal
以下の例を知って、OOPをやりたくなってもらう

- 複雑さを抑え込んだ変更に強いコード
- OOPらしく書くとコードが読みやすくなる
- 手続き型で作ると複雑さを抑え込めず読み難い

---
## Story
- サービスクラスをターゲットに手続き型で書いたコードとOOPなコードの複雑さの違いを測る
- ありそうな仕様変更を書いてみて違いを知る
	- その結果からOOPの効果の程を知る

---
## 具体例

---
## 要求仕様(ユーザストーリー)
- パスワード変更
  1. パスワードポリシーを満たせていたらパスワードを変更する
  1. 満たせていない場合はエラーを返す

---
### パスワードポリシー
1. パスワードの長さは8文字以上20文字以内
1. 英数字大文字小文字を最低１回使用する
1. ユーザ名と一致しない
1. 現在のパスワードと一致しない
1. 氏名(アルファベット)を含まない
   - 大文字小文字は区別しない
1. メールアドレスと一致しない
1. 電話番号と一致しない

---
### サンプルコード

#### おことわり
- 画面表示の都合上、要点に絞って表示しています

---
#### Procedural

---
##### Production Code
```java
public void changePassword(String id,String newPassword){
    User user = this.userRepository.userFromId(
                        UUID.fromString(id));
    if (newPassword.length() < 8 
            || newPassword.length() > 20) {
        String msg = "inputted password violated"
                        + "password length policy";
        throw new ViolatedPasswordPolicyException(msg);
    } // omitted the middle, coz it's too long.
    if (INCLUDING_UPPER_CASE_ALPHABET_AT_LEAST_ONE
            .matcher(newPassword).find() == false) {
        String msg = "inputted password violated"
                        + "character policy";
        throw new ViolatedPasswordPolicyException(msg);
    }
    this.userRepository.updatePassword(user.getId(),
                            new Password(newPassword));
}
```
---
##### 特徴
- ポリシーを満たすかチェックするif文が10個以上並んでいる(例では略した)
- ポリシーにルールが増えるたびにif文が増える
- つまり、パスワード変更というユーザストーリーの中にパスワードポリシーの詳細が漏れている

---
#### OOP

---
##### Production Code
```java
public void changePassword(ChangePasswordCommand command) {

    UUID id = command.getId();
    User user = this.userRepository.userFromId(id);

    PasswordPolicy policy = this.policyFactory
                        .generatePasswordPolicyFor(user);
    Password password = new Password(
                                command.getNewPassword());

    this.policy.validate(password);

    this.userRepository.updatePassword(id, password);
}
```

---
##### 特徴
- 分岐(if文等)がない
- パスワードポリシーの詳細をサービスクラスが知らない（知らなくて済む）
- ポリシーのみを切り離して単体テストが可能

---
#### サンプルコードの複雑さを観測してみる
- Cyclotic Complexityについては後述

|Procedure/OOP|Cyclotic Complexity|
|:--|:--|
|Procedure|13|
|OOP|1|

- 手続き型も13なら、悪くはない
- OOPは1と最小値
  - 依存しているクラスも全部値が小さい

---
#### What's cyclomatic complexity
- コードの複雑さを表す指標
- if文などで分岐が増えるとポイントが1増える
- if文やswitchがなければcyclomatic complexityは1(最小値)
- 分岐がなくてもメソッドが2つ生えてたら最低で2
- 参考URL(不正確らしいがわかりやすいので紹介)
    - http://saturday-dory-fever.blogspot.jp/2012/04/cyclomatic-complexity.html

---
##### 目安
|循環的<br>複雑度|複雑さの状態|バグ<br>混入確率|
|:--|:--|:--|
|10以下|非常に良い構造|25％|
|30以上|構造的なリスクあり|40％|
|50以上|テスト不可能|70％|
|75以上|いかなる変更も誤修正を生む|98％|

- 参考
  - http://szk-takanori.hatenablog.com/entry/20111219/p1

---
#### サンプルコードの複雑さを観測してみる(再掲)
|Procedure/OOP|Cyclotic Complexity|
|:--|:--|
|Procedure|13|
|OOP|1|

- 現時点ではどちらも悪くはない
- OOPで書いたほうが指標で判断すれば優れていると言える

---
## 仕様変更 is coming !

---
### ユーザ名も変更可能にしたい
- パスワードと同じポリシーを適用したい

---
#### Procedural

---
##### 修正ポイント
1. `changePassword`をコピペして`changeUsername`を作成
1. `changePassword`と`changeUsername`の重複を排除

---
##### Production Code
```java
public void changeUsername(User user,String newUsername){
    User user = this.userRepository
            .userFromId(UUID.fromString(id));
    this.validateCommonPolicy(user, 
                            newPassword, "password");
    this.userRepository.updatePassword(user.getId(),
                            new Password(newPassword));
}
private void validateCommonPolicy(User user,
            String newAuthenticationFactor, String name) {
    if (newAuthenticationFactor.length() < 8
        || newAuthenticationFactor.length() > 20) {
            log.warn("inputted password violated"
                + "password length policy");
       throw new ViolatedPasswordPolicyException(msg);
    } // omitted, a lot.
}
```

---
#### OOP

---
##### 修正ポイント
1. `PasswordPolicy`をコピペして`UsernamePolicy`を作成
1. `generatePasswordPolicyFor`をコピペして`generateUsernamePolicyFor`を作成
1. `changePassword`をコピペして`changeUsername`を作成
1. `PasswordPolicy`と`UsernamePoilcy`の重複を排除

---
##### Production Code
```java
public void changeUsername(ChangeUsernameCommand command) {
    UUID id = command.getId();
    User user = this.userRepository.userFromId(id);

    UsernamePolicy policy = this.policyFactory
                        .generateUsernamePolicyFor(user);
    Username username = new Username(
                                command.getNewUsername());
    this.policy.validate(username);
    this.userRepository.updateUsername(id, username);
}
```
---
### ユーザ名の文字長の制限は<br>4文字以上10文字以内にしたい

---
#### Procedure

---
##### 修正ポイント
- 文字長チェックのif分をvalidateCommonPolicyからchangePasswordとchangeUsernameにコピー
- usernameの文字長下限のチェックを8から4に変更
---
##### Production Code
```java
public void changeUsername(String id, String newUsername) {
    // omitted
    if (newUsername.length() < 4
        || newUsername.length() > 20) {
        String msg = "inputted uername violated"
                       + "username length policy";
        log.warn(msg); // it's warn just for testing.
        throw new ViolatedPolicyException(msg);
    }
    this.validateCommonPolicy(newUsername,
                                user,
                                "username");
    this.userRepository.updateUsername(
                            user.getId(),
                            new Username(newUsername));
}
```
---
#### OOP

---
##### 修正ポイント
- LengthPolicyをcommonPolicyから両方にコピペ
- usernameの方だけ文字長を8から4に変更

---
##### Production Code
```java
public PasswordPolicy generatePasswordPolicyFor(User user) {
    Set<Policy> policies = generateCommonPolicy(user);
    policies.add(new LengthPolicy(8, 20));

    return new PasswordPolicy(policies);
}

public UsernamePolicy generateUsernamePolicyFor(User user) {
    Set<Policy> policies = generateCommonPolicy(user);
    policies.add(new LengthPolicy(4, 20));

    return new UsernamePolicy(policies);
}
```
---
### ユーザ名はメールアドレスとの重複可

---
#### Procedure

---
##### 修正ポイント
- メールアドレスとの一致チェックのif文をvalidateCommonPolicyからchangePasswordに移動
---
##### Production Code
```java
public void changePassword(String id, String newPassword) {
    // omitted
    if (newPassword.length() < 8
        || newPassword.length() > 20) {
        String msg = "inputted password violated password"
                        + "length policy";
        log.warn(msg); // it's warn just for testing.
        throw new ViolatedPolicyException(msg);
    }
    if (newPassword.equals(user.getPerson()
        .getContactInformation()
        .getMailAddress().getValue())) {
        String msg = "inputted password violated"
                    + "not same with mail address policy";
        log.warn(msg);
        throw new ViolatedPolicyException(msg);
    } // omitted, a lot.
}
```
---
#### OOP

---
##### 修正ポイント
- NotSameWithMailAddressPolicyをgenerateCommonPolicyから移動

---
##### Production Code
```java
public PasswordPolicy generatePasswordPolicyFor(User user) {
    Set<Policy> policies = generateCommonPolicy(user);

    policies.add(new LengthPolicy(8, 20));
    policies.add(new NotSameWithMailAddressPolicy(
                    user.getPerson()
                            .getContactInformation()
                            .getMailAddress()));

    return new PasswordPolicy(policies);
}

```
---
#### Cyclotic Complexityがどうなったか

|Procedure/OOP|Cyclotic Complexity|
|:--|:--|
|Procedure|17|
|OOP|2|

- 仕様変更が入った分だけ、手続き型は複雑になっている
- OOPはメソッドの数と同値
  - 一つのメソッドあたりの複雑さは1のまま

---
## まとめ

---
### 手続き型を振り返ると
- 共通ロジックをメソッドにくくりだすことで重複が排除できた
  - コードの重複は排除出来たが複雑度が減ったわけではない
- ユーザ名とパスワードのポリシーの違いが増えるたびに複雑度は増える

---
### OOPらしく書くと何が違ったか？
- ユーザ名を変更するメソッドが増えても複雑度は1しかあがらない
- パスワードポリシーのルールが変わったとしてもサービスクラス（ビジネスロジック）に影響がない
- 1つ1つの処理（今回はポリシー）に名前がつくので全体的に読みやすい

---
### OOPらしく書くと何が違ったか？
- サービスクラスがシンプル(分岐がない)にないので見通しが良い
- 利用される側であるPolicyやFactoryについてもポリシーの仕様変更に影響してcyclomatic complexityのポイントがあがることはないということにも注目してほしい

---

### 注意点
- cyclomatic complexityはあくまで指標
  - 業務の複雑さはコードの書き方を工夫したところでなくせない
  - 狭い範囲に閉じ込めることで変更に強くすることはできる
- 業務ルールがカプセル化されることにより利用側のクラスがその詳細を知らずに済むことが大切
  - その分、変更に強くなる

---
### 例えば今回は
#### ポリシーをオブジェクトにしたことで
- パスワードリセットなど、パスワードを変更する他の機能でも利用できる(再利用性)
- メールアドレスのアカウント部と一致してはいけない、などのポリシーの変更に強い(変更容易性)

---
### オブジェクト指向プログラミングで、将来の自分に楽をさせてあげましょう
---
## Let's do the OOP !

---
## Fin